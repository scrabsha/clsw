use std::collections::HashMap;
use std::time::Instant;

use once_cell::sync::OnceCell;
use tokio::sync::Mutex;

use warp::{hyper::Method, reject, Filter, Rejection};

static DB: OnceCell<Mutex<HashMap<String, Instant>>> = OnceCell::new();

async fn run_db_transaction<T>(f: impl FnOnce(&mut HashMap<String, Instant>) -> T) -> T {
    let mut guard = DB.get_or_init(|| Mutex::new(HashMap::new())).lock().await;

    f(&mut guard)
}

#[tokio::main]
async fn main() {
    println!("Hello, world!");

    let cors = warp::cors()
        .allow_any_origin()
        .allow_methods([Method::GET, Method::POST]);

    let get = warp::get()
        .and(warp::path!("scrabsha" / String))
        .and_then(|name: String| async move {
            run_db_transaction(|db| {
                db.get(&name)
                    .map(|instant| instant.elapsed().as_secs().to_string())
            })
            .await
            .ok_or_else(warp::reject)
        })
        .boxed();

    let post = warp::post()
        .and(warp::path!("scrabsha" / String))
        .and_then(|name: String| async move {
            run_db_transaction(|db| {
                db.insert(name, Instant::now());
            })
            .await;
            Ok::<_, Rejection>("ok")
        })
        .boxed();

    let catchall = warp::any().and_then(|| async { Ok::<&str, Rejection>("\"Nope\"") });

    let routes = get.or(post).or(catchall).with(cors);

    let server = warp::serve(routes);

    server.run(([0, 0, 0, 0], 3030)).await;
}
