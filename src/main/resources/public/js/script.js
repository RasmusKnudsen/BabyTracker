document.getElementById("trackingForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const entry = {
        type: document.getElementById("type").value,
        value: document.getElementById("value").value,
        time: document.getElementById("time").value
    };

    await fetch("/babytracking", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(entry)
    });

    loadEntries();
});

async function loadEntries() {
    const response = await fetch("/babytracking");
    const entries = await response.json();

    const entriesList = document.getElementById("entries");
    entriesList.innerHTML = "";
    entries.forEach(entry => {
        const li = document.createElement("li");
        li.textContent = `${entry.time}: ${entry.type} - ${entry.value}`;
        entriesList.appendChild(li);
    });
}

loadEntries();
