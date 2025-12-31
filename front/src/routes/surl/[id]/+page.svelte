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

    $effect(() => {
        getSurl();
    });
</script>

<h1>{$page.params.id}번 SURL 페이지</h1>

{#if surl}
    <div>
        ID: {surl.id}
    </div>

    <div>
        URL: {surl.url}
    </div>

    <div>
        내용: {surl.body}
    </div>

    <div>
        생성일: {surl.createDate}
    </div>

    <div>
        수정일: {surl.modifyDate}
    </div>
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
