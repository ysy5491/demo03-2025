<script lang="ts">
	import rq from "$lib/rq/rq.svelte";

    async function submitAddForm(this : HTMLFormElement) {
        const form: HTMLFormElement = this;

        form.url.value = form.url.value.trim();
        
        if (form.url.value.length === 0) {
            alert("url을 입력해주세요.");
            form.url.focus();
            return;
        }

        form.body.value = form.body.value.trim();

        if (form.body.value.length === 0) {
            alert("내용을 입력해주세요.");
            form.body.focus();
            return;
        }

        const {data, error} = await rq.getClient().POST('/api/v1/surls', {
            body: {
                url: form.url.value,
                body: form.body.value
            }
        });
        
        if (data) {
            console.log('SURL 생성 성공', data);
            alert('SURL 생성에 성공했습니다.');
            rq.replace('/surl/list'); // 생성 후 목록 페이지로 이동
        } else if (error) {
            console.error('SURL 생성 실패', error);
            alert('SURL 생성에 실패했습니다: ' + error.msg);
        }
    }
</script>

<h1>SURL 생성</h1>

<form on:submit|preventDefault={submitAddForm}>   
    <div>
        <label>URL</label>
        <input type="url" name="url" placeholder="URL"/>
    </div>
    <div>
        <label>내용</label>
        <input type="text" name="body" placeholder="내용"/>
    </div>
    <div>
        <label>등록</label>
        <input type="submit" value="등록">
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