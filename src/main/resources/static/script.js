const fetcher = async (event) => {
    const form = event.target
    const button = form.querySelector('button[type="submit"]')
    const text = button.textContent
    button.disabled = true
    button.textContent="Loading..."
    try {
        const response = await fetch(form.action, {
            method: form.method,
            body: form.method == 'post' ? new FormData(form): null,
            });
        console.log(await response.ok);
        button.textContent = await text
        await form.reset()
        button.disabled = await false
        if (await !response.ok) {
            alert("Invalid inputs. Please try again.")
        }

    } catch (e) {
        console.error(e);
    }
}

const downloader = (event) => {
    const a = document.createElement("a");
    a.href = event.target.action
    a.download = ''
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
}

document.addEventListener('submit', (event) => {
    if(event.target.classList.contains('download')){
        downloader(event)
        return
    }
    fetcher(event)
    event.preventDefault()
})