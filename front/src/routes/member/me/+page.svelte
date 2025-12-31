<script lang="ts">
    import type { components } from '$lib/backend/apiV1/schema';
    import rq from "$lib/rq/rq.svelte";
    
    let member: components['schemas']['MemberDto'] | null = $state(null);
    let errorMEssage: string | null = $state(null);

    async function getME() {
        const {data, error} = await rq.getClient().GET('/api/v1/members/me');
        
        if (data) {
            member = data.data.item
            errorMEssage = null;
        } else if (error) {
            errorMEssage = '내 정보 조회에 실패했습니다: ' + error.msg;
        }
    }

    $effect(() => {
        getME();
    });
</script>

<h1>내 정보</h1>

{#if rq.isLogin()}
    <h1>내 정보</h1>
    <p>아이디: {rq.member.username}</p>
    <p>닉네임: {rq.member.nickname}</p>
    <p>생성일: {rq.member.createDate}</p>
    <p>수정일: {rq.member.modifyDate}</p>
{:else if errorMEssage}
    <h1>오류 발생</h1>
    <p>{errorMEssage}</p>
{:else}
    <h1>로딩 중...</h1>
{/if}


<style>
    h1 {
        font-size: 2rem;
        color: #333;
        text-align: center;
        margin-bottom: 1rem;
    }

    p {
        font-size: 1.2rem;
        color: #555;
        margin: 0.5rem 0;
        text-align: center;
    }


    h1, p {
        margin: 0;
    }

    h1 + p {
        margin-top: 1rem;
    }
</style>