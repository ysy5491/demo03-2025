import createClient from 'openapi-fetch';

import type { components, paths } from '$lib/backend/apiV1/schema';
import { goto } from '$app/navigation';

type Client = ReturnType<typeof createClient<paths>>;

const client: Client = createClient<paths>({
    baseUrl: import.meta.env.VITE_CORE_API_BASE_URL,
    credentials: 'include'
});
class Rq {
    public getClient() {
        return client;
    }

    public goto(url: string) {
        goto(url);
    }
}

const rq = new Rq();

export default rq;