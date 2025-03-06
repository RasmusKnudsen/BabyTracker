document.getElementById("amningForm").addEventListener("submit", async function(event) {
    event.preventDefault(); // Forhindrer siden i at reloade

    const startTime = document.getElementById("startTime").value + ":00";
    const endTime = document.getElementById("endTime").value + ":00";
    const leftBreastTime = parseInt(document.getElementById("leftBreastTime").value) || 0;
    const rightBreastTime = parseInt(document.getElementById("rightBreastTime").value) || 0;
    const lastBreastUsed = document.getElementById("lastBreastUsed").value;

    const totalTime = leftBreastTime + rightBreastTime; // Beregn samlet amningstid

    const entry = {
        type: "Amning",
        startTime: startTime,
        endTime: endTime,
        totalTime: totalTime,
        leftBreastTime: leftBreastTime,
        rightBreastTime: rightBreastTime,
        lastBreastUsed: lastBreastUsed
    };

    console.log("DEBUG: Data sendt til serveren ->", JSON.stringify(entry)); // 🔍 Debug JSON

    try {
        const response = await fetch("/amning", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(entry)
        });

        console.log("DEBUG: Server-respons status ->", response.status); // 🔍 Debug status

        if (response.ok) {
            console.log("✅ Amning registreret!");
            alert("✅ Amning registreret!");
            document.getElementById("amningForm").reset(); // Ryd formularen efter succes
        } else {
            const errorText = await response.text();
            console.error("❌ Fejl fra serveren:", errorText); // 🔍 Debug fejl fra serveren
            alert("❌ Fejl: " + errorText);
        }
    } catch (error) {
        console.error("❌ Fejl:", error);
    }
});
