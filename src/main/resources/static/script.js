document.getElementById("getWorkout").addEventListener("click", function() {
    const type = document.getElementById("type").value;
    const level = document.getElementById("level").value;

    const url = `/api/workout?type=${type}&level=${level}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const workoutList = document.getElementById("workoutList");
            workoutList.innerHTML = "";

            if (data.workout && data.workout.length > 0) {
                data.workout.forEach(item => {
                    const li = document.createElement("li");
                    li.textContent = item;
                    workoutList.appendChild(li);
                });
            } else {
                const li = document.createElement("li");
                li.textContent = "No se encontró una rutina para estos parámetros.";
                workoutList.appendChild(li);
            }
        })
        .catch(error => {
            console.error("Error al obtener la rutina:", error);
        });
});
