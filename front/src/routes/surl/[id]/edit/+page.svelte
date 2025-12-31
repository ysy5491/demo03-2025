<script lang="ts">
    import { page } from "$app/stores";
    import type { components } from "$lib/backend/apiV1/schema";
	import rq from "$lib/rq/rq.svelte";

    let surl = $state<components['schemas']['SurlDto'] | null>(null);
    let errorMessage = $state<string | null>(null);
    async function getSurl() {
        const {data, error} = await rq.getClient().GET('/api/v1/surls/{id}', {
            params: {
                path: {
                    id: $page.params.id ? parseInt($page.params.id) : 0
                }
            }
        });

        if (data) {
            surl = data.data.item;
        } else if (error) {
            // error.msg && alert('단축 URL 조회에 실패했습니다: ' + error.msg);
            errorMessage = error.msg;
        }
    }

    async function submitModifyForm(this : HTMLFormElement, event: Event) {
        event.preventDefault();
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

        const {data, error} = await rq.getClient().PUT('/api/v1/surls/{id}', {
            params: {
                path: {
                    id: $page.params.id ? parseInt($page.params.id) : 0
                }
            },
            body: {
                url: form.url.value,
                body: form.body.value
            }
        });
        
        if (data) {
            console.log('SURL 수정 성공', data);
            alert('SURL 수정에 성공했습니다.'); // 수정 성공 메시지로 변경
            rq.replace('/surl/list'); // 수정 후 목록 페이지로 이동
        } else if (error) {
            console.error('SURL 수정 실패', error); // 수정 실패 메시지로 변경
            alert('SURL 수정에 실패했습니다: ' + error.msg); // 수정 실패 메시지로 변경
        }
    }

    $effect(() => {
        getSurl();
    });
</script>

<h1>{$page.params.id}번 SURL 수정</h1>

{#if surl}
    <form onsubmit={submitModifyForm}>   
        <div>
            <label>URL</label>
            <input type="url" name="url" placeholder="URL" value="{surl.url}"/>
        </div>
        <div>
            <label>내용</label>
            <input type="text" name="body" placeholder="내용" value="{surl.body}"/>
        </div>
        <div>
            <label>수정</label>
            <input type="submit" value="수정">
            <button type="button" onclick={() => history.back()}>뒤로가기</button> 
        </div> 
    </form>
{:else if errorMessage}
    <div>
        {errorMessage}
    </div>
{/if}

<style>
    h1 {
        font-size: 2rem;
        color: #333;
        text-align: center;
        margin-bottom: 1rem;
    }

    div {
        font-size: 1.2rem;
        color: #555;
        margin: 0.5rem 0;
        text-align: center;
    }
    h1, div {
        margin: 0;
    }   
    h1 + div {
        margin-top: 1rem;
    }
</style>
