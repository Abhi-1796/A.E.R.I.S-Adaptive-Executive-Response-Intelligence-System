function sendCommand() {
    const cmd = document.getElementById("command").value;

    fetch("/api/aeris/command", {
        method: "POST",
        headers: {
            "Content-Type": "text/plain"
        },
        body: cmd
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById("response").innerText = data;
    })
    .catch(error => {
        document.getElementById("response").innerText = "Error connecting to AERIS";
    });
  function deleteHistory(id) {
      console.log("Deleting ID:", id); // 👈 ADD THIS

      fetch(`/api/aeris/history/${id}`, {
          method: "DELETE"
      })
      .then(() => loadHistory());
  }
}