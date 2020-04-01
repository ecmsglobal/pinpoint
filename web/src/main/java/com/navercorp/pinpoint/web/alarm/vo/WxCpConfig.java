package com.navercorp.pinpoint.web.alarm.vo;

import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.WxCpConfigStorage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhgLiu
 */
@Configuration
public class WxCpConfig {

    private String CORP_ID = "wxd9a4122a408eb831";
    private String CORP_SECRET = "P1zLmjXq_9r14HCA63HmsBvWzA0NPNqbRATd08VzyS8";
    private int AGENT_ID = 1000019;
    private String AES_KEY = "1231231231";

    @Bean
    WxCpConfigStorage wxCpConfigStorage() {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        // 设置微信企业号的appid
        config.setCorpId(CORP_ID);
        // 设置微信企业号的app corpSecret
        config.setCorpSecret(CORP_SECRET);
        // 设置微信企业号应用ID
        config.setAgentId(AGENT_ID);
        // 设置微信企业号应用的token
//        config.setToken(TOKEN);
        // 设置微信企业号应用的EncodingAESKey
        config.setAesKey(AES_KEY);
        return config;
    }

    @Bean
    WxCpService wxCpService(@Autowired WxCpConfigStorage wxCpConfigStorage) {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(wxCpConfigStorage);
        return wxCpService;
    }


}
