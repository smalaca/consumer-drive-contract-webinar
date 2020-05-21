package com.smalaca.accountmanagement.domain.account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findByOwnerId(Long ownerId);

    Optional<Account> findByNumber(String number);
}
