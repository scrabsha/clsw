'use strict';

const enterButton = document.querySelector('.btn-enter');
const returnButton = document.querySelector('.btn-return');

const input_div = document.querySelector('.input_watch');
const show_div = document.querySelector('.show_watch');
const incorrect_input = document.querySelector('.incorrect_input');

const tiler = document.querySelector('#timer');

var date_timer = new Date(0, 0, 0, 0, 0, 0, 0);
var date_timer_interval;
var watch_id;

var last_value = 0;

async function timer() {
    if (await has_moved()) {
        console.log('moved');
        date_timer = new Date(0, 0, 0, 0, 0, 0, 0);
    }

    date_timer.setSeconds(date_timer.getSeconds() + 1);
    tiler.innerHTML = date_timer.toLocaleTimeString();

    if (date_timer.getSeconds() % 1800 == 0) {
        if (Notification?.permission === "granted") {

            new Notification(`You haven't moved much!`, { body: `It has been ${date_timer.toLocaleTimeString()} since you last moved.` });

        } else if (Notification && Notification.permission !== "denied") {
            Notification.requestPermission((status) => {
                // If the user said okay
                if (status === "granted") {
                    new Notification(`You haven't moved much!`, { body: `It has been ${date_timer.toLocaleTimeString()} since you last moved.` });
                } else {
                    alert(`You haven't moved much! It has been ${date_timer.toLocaleTimeString()} since you last moved`);
                }
            });
        } else {
            alert(`You haven't moved much! It has been ${date_timer.toLocaleTimeString()} since you last moved`);
        }
    }

}

async function has_moved() {
    var moved = false;
    var response = await fetch(`http://domino.zdimension.fr:3030/scrabsha/${watch_id}`);
    var data = await response.json();

    if (data < last_value) {
        moved = true;
    }
    last_value = data;
    return moved;

}

function start_timer() {
    date_timer = new Date(0, 0, 0, 0, 0, 0, 0);
    last_value = 0;
    tiler.innerHTML = new Date(date_timer).toLocaleTimeString();
    date_timer_interval = setInterval(timer, 1000);
}

function stop_timer() {
    clearInterval(date_timer_interval);
}


enterButton.addEventListener('click', async () => {
    if (Notification && Notification.permission !== "denied") {
        Notification.requestPermission()
    }

    const watch_id_field = document.querySelector('#watch_id');
    watch_id = watch_id_field.value;
    watch_id_field.value = '';
    try {
        fetch(`http://domino.zdimension.fr:3030/scrabsha/${watch_id}`).then(response => {
            if (response.ok) {
                input_div.style.display = 'none';
                show_div.style.display = 'block';
                start_timer();
            }
            else {
                input_div.style.display = 'none';
                incorrect_input.style.display = 'block';
            }
        });
    } catch (error) {
        input_div.style.display = 'none';
        incorrect_input.style.display = 'block';
    }
    /*
    input_div.style.display = 'none';
    show_div.style.display = 'block';
    start_timer();
    */
});

returnButton.addEventListener('click', () => {
    input_div.style.display = 'block';
    show_div.style.display = 'none';
    incorrect_input.style.display = 'none';
    stop_timer();
});