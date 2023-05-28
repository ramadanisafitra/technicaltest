package com.ramadanisafitra.technicaltest.repository;
import com.ramadanisafitra.technicaltest.model.CheckList;
import com.ramadanisafitra.technicaltest.model.CheckListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckListItemRepository extends JpaRepository<CheckListItem, Long> {

    List<CheckListItem> findByChecklist(CheckList checklist);

    Optional<CheckListItem> findByChecklistAndId(CheckList checklist, Long id);
}
