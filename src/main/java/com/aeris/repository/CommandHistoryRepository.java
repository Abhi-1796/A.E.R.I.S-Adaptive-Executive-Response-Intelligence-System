package com.aeris.repository;

import com.aeris.model.CommandHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandHistoryRepository extends JpaRepository<CommandHistory, Long> {
}
