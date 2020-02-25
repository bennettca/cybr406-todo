package com.cybr406.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoJpaRepository<T,ID> extends JpaRepository<T, ID>

{


}
