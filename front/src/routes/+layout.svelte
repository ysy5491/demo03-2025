<script lang="ts">
	import rq from "$lib/rq/rq.svelte";

    
    async function logout() {
        await rq.getClient().DELETE('/api/v1/members/logout');
        rq.setLogout();
        alert('로그아웃 되었습니다.');
        rq.goto('/');
    }
    
    // async function logout() { // async란 비동기 함수라는 뜻 비동기 함수는 작업이 완료될 때까지 기다리지 않고 다음 작업을 계속 진행
    //     const rs = await fetch(`${import.meta.env.VITE_CORE_API_BASE_URL}/api/v1/members/logout`, {
        //         method: 'DELETE',
        //         credentials: 'include', // 쿠키 먹기 위해 포함            
        //     }).then((res) => res.json());
        
        //     console.log(rs);
        // }
    // 사용자가 페이지를 새로고침해도 인증 상태를 유지하기 위해 initAuth 함수를 호출
    $effect(() => {
        rq.initAuth();
    });
    </script>
<slot />
<header>
    <nav>
        <a href="/">메인</a>
        {#if rq.isLogin()}
            <button type="button" on:click|preventDefault={logout}>로그아웃</button>
            <a href="/member/me">{rq.member.nickname}님의 정보</a>\
            <a href="/surl/list">단축 URL 목록</a>
            <a href="/surl/add">단축 URL 생성</a>
        {/if}
        {#if rq.isLogout()}
            <a href="/member/login">로그인</a>
            <a href="/member/join">회원가입</a>
        {/if}

        <a href="/todo">할 일 리스트</a>
    </nav>
</header>
