<script lang="ts">
	import rq from "$lib/rq/rq.svelte";

    async function submitJoinForm(this : HTMLFormElement) {
        const form: HTMLFormElement = this;

        form.username.value = form.username.value.trim();
        
        if (form.username.value.length === 0) {
            alert("아이디를 입력해주세요.");
            form.username.focus();
            return;
        }

        if (form.nickname.value.length === 0) {
            alert("닉네임을 입력해주세요.");
            form.nickname.focus();
            return;
        }
        
        form.password.value = form.password.value.trim();
        
        if (form.password.value.length === 0) {
            alert("비밀번호를 입력해주세요.");
            form.password.focus();
            return; 
        }

        form.passwordConfirm.value = form.passwordConfirm.value.trim();
        
        if (form.passwordConfirm.value.length === 0) {
            alert("비밀번호 확인을 입력해주세요.");
            form.passwordConfirm.focus();
            return; 
        }

        if (form.password.value !== form.passwordConfirm.value) {
            alert("비밀번호가 일치하지 않습니다.");
            form.passwordConfirm.focus();
            return; 
        }

        const {data, error} = await rq.getClient().POST('/api/v1/members', {
            body: {
                nickname: form.username.value,
                username: form.username.value,
                password: form.password.value
            }
        });
        
        if (data) {
            console.log('회원가입 성공', data);
            rq.setLogined(data.data.item)
            alert('회원가입에 성공했습니다.');
            rq.goto('/member/login');
        } else if (error) {
            console.error('회원가입 실패', error);
            alert('회원가입에 실패했습니다: ' + error.msg);
        }
        // const rs = await fetch(`${import.meta.env.VITE_CORE_API_BASE_URL}/api/v1/members/login`, {
        //     method: 'POST',
        //     credentials: 'include', // 쿠키 먹기 위해 포함
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body: JSON.stringify({
        //         username: form.username.value,
        //         password: form.password.value
        //     })
        // }).then((res) => res.json());
        // console.log(rs);
    }
</script>

<h1>회원가입</h1>

<form on:submit|preventDefault={submitJoinForm}>   
    <div>
        <label>아이디</label>
        <input type="text" name="username" placeholder="아이디"/>
    </div>
    <div>
        <label>닉네임</label>
        <input type="text" name="nickname" placeholder="닉네임"/>
    </div>
    <div>
        <label>비밀번호</label>
        <input type="password" name="password" placeholder="비밀번호"/>
    </div>
    <div>
        <label>비밀번호 확인</label>
        <input type="password" name="passwordConfirm" placeholder="비밀번호 확인">
    </div>
    <div>
        <label>회원가입</label>
        <input type="submit" value="회원가입">
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