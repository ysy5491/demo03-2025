<script lang="ts">
	import type { components } from "$lib/backend/apiV1/schema";
    import rq from "$lib/rq/rq.svelte";

    let surls = $state<components['schemas']['SurlDto'][]>([]); 

    async function getSurls() {
        const {data, error} = await rq.getClient().GET('/api/v1/surls');

        if (data) {
            surls = data.data.items;
        } else if (error) {
            alert('단축 URL 목록 조회에 실패했습니다: ' + error.msg);
        }
    }

    async function deleteSurl(surl: components['schemas']['SurlDto']) {
        const {data, error} = await rq.getClient().DELETE(`/api/v1/surls/{id}`, {
            params: {
                path: {
                    id: surl.id
                }
            }
        });

        if (data) { 
            surls.splice(surls.findIndex(s => s.id === surl.id), 1); // splice는 배열에서 요소를 제거 또는 교체하는 메서드 
        }
        else if (error) {
            error.msg && alert(error.msg);
        }
    }

    $effect(() => {
        getSurls();
    });
</script>

<h1>SURL 목록</h1>

<ul>
    {#each surls as surl (surl.id)}
        <li>
            <a href="/surl/{surl.id}">{surl.id}</a> : {surl.url} 
            <br>
            {surl.body}

            <button type="button" on:click={() => rq.goto(`/surl/${surl.id}/edit`)}>수정</button>
            <button type="button" on:click|preventDefault={() => confirm('정말로 삭제하시겠습니까?') && deleteSurl(surl)}>삭제</button> 
        </li>
    {/each}
        
</ul>