package com.byteasy.platform.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.byteasy.platform.entity.User;
import com.byteasy.platform.entity.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
public class PlatformUserDetails extends User implements UserDetails {

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return (getAccountExpiredAt() == null || getAccountExpiredAt().isAfter(LocalDateTime.now()));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !UserStatus.LOCKED.equals(getStatus());
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return (getCredentialsExpiredAt() == null || getCredentialsExpiredAt().isAfter(LocalDateTime.now()));
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return UserStatus.NORMAL.equals(getStatus());
    }

    /**
     * 这里如果不重写hashcode和equals，oidc的会话session是以此对象为hashmap的key，造成会话匹配不是。
     * @param o   the reference object with which to compare.
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlatformUserDetails that = (PlatformUserDetails) o;
        // 假设User类有getId()方法返回用户唯一ID
        return getUsername() != null && getUsername().equals(that.getUsername());
    }

    /**
     * 核心：相等的对象必须有相同的hashCode
     * 只基于用户ID生成哈希值
     */
    @Override
    public int hashCode() {
        // 假设User类有getId()方法
        return getUsername() != null ? getUsername().hashCode() : 0;
    }

}
