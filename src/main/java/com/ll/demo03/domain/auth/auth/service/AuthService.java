package com.ll.demo03.domain.auth.auth.service;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.surl.surl.entity.Surl;
import com.ll.demo03.global.exceptions.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    // =============Surl 권한 시작=================
    public void checkCanGetSurl(Member actor, Surl surl) {
        if(!canGetSurl(actor, surl))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    private boolean canGetSurl(Member actor, Surl surl) {
        if(actor == null) return false;
        if(surl == null) return false;

        // actor와 surl작성자의 actor가 같으면 true 반한, 즉 앞에 !때문에 작성자가 같다면 false 반환
        return actor.equals(surl.getAuthor());
    }

    public void checkCanDeleteSurl(Member actor, Surl surl) {
        if(!canDeleteSurl(actor, surl))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    private boolean canDeleteSurl(Member actor, Surl surl) {
        if(actor == null) return false;
        if(surl == null) return false;

        // actor와 surl작성자의 actor가 같으면 true 반한, 즉 앞에 !때문에 작성자가 같다면 false 반환
        return actor.equals(surl.getAuthor());
    }

    public void checkCanModifySurl(Member actor, Surl surl) {
        if(!canDeleteSurl(actor, surl))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    private boolean canModifySurl(Member actor, Surl surl) {
        if(actor == null) return false;
        if(surl == null) return false;

        // actor와 surl작성자의 actor가 같으면 true 반한, 즉 앞에 !때문에 작성자가 같다면 false 반환
        return actor.equals(surl.getAuthor());
    }
    // =============Surl 권한 종료=================

}
