package com.cybr406.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskJpaRepository<T,ID> extends JpaRepository<T, ID>
{

}
