package com.zongkx.dao;

import com.zongkx.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @author zongkxc
 */

@Service
public interface PolicyDAO extends JpaRepository<Policy, String> {
}
