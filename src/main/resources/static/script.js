const fetcher = async (event) => {
    const form = event.target
    const loader = document.querySelector('#loading-'+form.id)
    const defaultDisplay = form.style.display
    loader.hidden=false
    form.style.display="none"

    try {
        const response = await fetch(form.action, {
            method: form.method,
            body: form.method == 'post' ? new FormData(form): null,
            });
        console.log(await response.ok);
        loader.hidden = await true
        form.style.display=await defaultDisplay
        await form.reset()
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
    if(event.target.className=='download'){
        downloader(event)
        return
    }
    fetcher(event)
    event.preventDefault()
})