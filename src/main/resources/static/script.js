document.getElementById("getWorkout").addEventListener("click", function() {
    const type = document.getElementById("type").value;
    const level = document.getElementById("level").value;

    fetch(`/api/workout?type=${type}&level=${level}`)
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

// Nueva función para agregar ejercicios
document.getElementById("addWorkout").addEventListener("click", function() {
    const type = document.getElementById("type").value;
    const level = document.getElementById("level").value;
    const exercise = document.getElementById("exercise").value;

    if (exercise.trim() === "") {
        alert("Por favor, ingresa un ejercicio válido.");
        return;
    }

    fetch("/api/workout", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ type, level, exercise })
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message || "Ejercicio agregado correctamente.");
        document.getElementById("exercise").value = "";
    })
    .catch(error => {
        console.error("Error al agregar el ejercicio:", error);
    });
});
