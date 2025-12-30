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

    public member: components['schemas']['MemberDto'];

    constructor() {
        this.member = this.makeReactivityMember();
    }


    public makeReactivityMember() {
        let id = $state(0);
        let username = $state('');
        let nickname = $state('');
        let createDate = $state('');
        let modifyDate = $state('');
    
        return {
          get id() {
            return id;
          },
          set id(value: number) {
            id = value;
          },
          get createDate() {
            return createDate;
          },
          set createDate(value: string) {
            createDate = value;
          },
          get modifyDate() {
            return modifyDate;
          },
          set modifyDate(value: string) {
            modifyDate = value;
          },
          get username() {
            return username;
          },
          set username(value: string) {
            username = value;
          },
          get nickname() {
            return nickname;
          },
          set nickname(value: string) {
            nickname = value;
          }
        };
    }
    
    public isLogin() {
        return this.member.id !== 0;
    }

    public setLogined(member: components['schemas']['MemberDto']) {
        Object.assign(this.member, member);
    }
    
    public isLogout() {
        return !this.isLogin();
    }
    

    public setLogout() {
        this.member.id = 0;
        this.member.createDate = '';
        this.member.modifyDate = '';
        this.member.username = '';
        this.member.nickname = '';
    }

    public apiEndPoints() {
        return createClient<paths>({
          baseUrl: import.meta.env.VITE_CORE_API_BASE_URL,
          credentials: 'include'
        });
      }
    
      public apiEndPointsWithFetch(fetch: any) {
        return createClient<paths>({
          baseUrl: import.meta.env.VITE_CORE_API_BASE_URL,
          credentials: 'include',
          fetch
        });
      }

    public async initAuth() {
        const { data } = await this.apiEndPoints().GET('/api/v1/members/me');
    
        if (data) {
          this.setLogined(data.data.item);
        }
    }
}

const rq = new Rq();

export default rq;