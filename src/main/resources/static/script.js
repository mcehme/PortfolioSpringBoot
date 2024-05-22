const fetcher = async (event, token) => {
    const form = event.target
    const button = form.querySelector('button[type="submit"]')
    const text = button.textContent
    const data = new FormData(form)
    data.append("response", token)
    data.append("action", "SUBMIT")
    button.disabled = true
    button.textContent='Loading...'
    try {
        const response = await fetch(form.action, {
            method: form.method,
            body: form.method == 'post' ? data: null,
            });
        console.log(await response.ok);
        const blob = await response.blob()
        const filename = form.id
        if (await blob.size) {
            const url = window.URL.createObjectURL(await blob)
            downloader(await url, filename)
        }
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

const downloader = (url, filename) => {
    const a = document.createElement("a");
    a.href = url
    a.download = filename
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
}

document.addEventListener('submit', (event) => {
        event.preventDefault()
        onClick(event)
        })


const onClick = (e) => {
    grecaptcha.ready(() => {
        grecaptcha.execute('6Lc99eMpAAAAAKigJpRGGitIMj6iGaJpu6-yP8Ot', {action: 'SUBMIT'}).then((token) => {
            fetcher(e, token)
        })
    })
}