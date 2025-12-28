<script lang="ts">
    async function submitLoginForm(this : HTMLFormElement) {
        const form: HTMLFormElement = this;

        form.username.value = form.username.value.trim();
        
        if (form.username.value.length === 0) {
            alert("아이디를 입력해주세요.");
            form.username.focus();
            return;
        }
        
        form.password.value = form.password.value.trim();
        
        if (form.password.value.length === 0) {
            alert("비밀번호를 입력해주세요.");
            form.password.focus();
            return; 
        }

        const rs = await fetch(`${import.meta.env.VITE_CORE_API_BASE_URL}/api/v1/members/login`, {
            method: 'POST',
            credentials: 'include', // 쿠키 먹기 위해 포함
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: form.username.value,
                password: form.password.value
            })
        }).then((res) => res.json());
        console.log(rs);
    }
</script>

<form on:submit|preventDefault={submitLoginForm}>   
    <div>
        <label>아이디</label>
        <input type="text" name="username" placeholder="아이디"/>
    </div>
    <div>
        <label>비밀번호</label>
        <input type="password" name="password" placeholder="비밀번호"/>
    </div>
    <div>
        <label>회원가입</label>
        <input type="submit" value="회원가입">
    </div>
</form>

<style>
    form {
        display: flex;
        flex-direction: column;
        width: 300px;
        margin: 0 auto;
    }
    div {
        margin-bottom: 15px;
    }
    label {
        display: block;
        margin-bottom: 5px;
    }
    input[type="text"],
    input[type="password"] {
        width: 100%;
        padding: 8px;
        box-sizing: border-box;
    }
    input[type="submit"] {
        padding: 10px;
        background-color: #4CAF50;
        color: white;
        border: none;
        cursor: pointer;
    }
    input[type="submit"]:hover {
        background-color: #45a049;
    }
</style>