<script lang="ts">

    import createClient from 'openapi-fetch';

    import type { paths } from '$lib/backend/apiV1/schema';

    type Client = ReturnType<typeof createClient<paths>>;

    const client: Client = createClient<paths>({
        baseUrl: import.meta.env.VITE_CORE_API_BASE_URL,
        credentials: 'include',
    });

    class GlobalError extends Error {
        rs: any;

        constructor(rs: any) {
            super(rs.msg);
            this.rs = rs;
        }
    }

    type Member = {
        id: number;
        createDate: string;
        modifyDate: string;
        username: string;
        nickname: string;
    };

    let member: Member | null = $state(null);
    let errorMEssage: string | null = $state(null);

    async function getME() {
        try {
            const { data, error } = await client.GET('/api/v1/members/me');

            if (data) {
                member = data.item;
            } else if (error) {
                throw new GlobalError(error);
            }
        } catch (error: any) {
            errorMEssage = error.rs.msg;
            console.error(error.rs);
        }
    }
    // async function getME() {
    //     try {
    //         const response = await fetch(`${import.meta.env.VITE_CORE_API_BASE_URL}/api/v1/members/me`, {
    //             credentials: 'include' // 쿠키 먹기 위해 포함
    //         });

    //         const rs = await response.json();

    //         if (!response.ok) {
    //             throw new GlobalError(rs);
    //         }

    //         member = rs.data.item
    //     } catch (error: any) {
    //         errorMEssage = error.rs.msg;
    //         console.error(error.rs);
    //     }
    // }

    $effect(() => {
        getME();
    });
</script>

{#if member}
    <h1>내 정보</h1>
    <p>아이디: {member.username}</p>
    <p>닉네임: {member.nickname}</p>
    <p>가입일: {new Date(member.createDate).toLocaleString()}</p>
    <p>수정일: {new Date(member.modifyDate).toLocaleString()}</p>
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