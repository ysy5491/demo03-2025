<script>
	let title = "지환이가 만든 홈페이지에 오신 것을 환영합니다!";
	let description = "스프링 부트와 SvelteKit을 사용하여 제작된 멋진 웹사이트입니다.";
</script>

<style>
	main {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 100vh;
		background: linear-gradient(135deg, #6a11cb, #2575fc);
		color: white;
		font-family: 'Arial', sans-serif;
		text-align: center;
		padding: 20px;
	}

	h1 {
		font-size: 3rem;
		margin-bottom: 1rem;
	}

	p {
		font-size: 1.5rem;
		margin-bottom: 2rem;
	}

	button {
		padding: 10px 20px;
		font-size: 1rem;
		color: white;
		background-color: #ff6f61;
		border: none;
		border-radius: 5px;
		cursor: pointer;
		transition: background-color 0.3s ease;
	}

	button:hover {
		background-color: #ff3b2f;
	}
</style>

<main>
	<h1>{title}</h1>
	<p>{description}</p>
	<button on:click={() => alert('버튼을 클릭하셨네요!')}>Get Started</button>
</main>