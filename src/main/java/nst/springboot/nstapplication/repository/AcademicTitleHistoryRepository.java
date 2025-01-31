package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.AcademicTitleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicTitleHistoryRepository extends JpaRepository<AcademicTitleHistory, Long> {

    List<AcademicTitleHistory> findAllByMemberIdOrderByStartDateDesc(Long memberId);

    @Query("SELECT at FROM AcademicTitleHistory at " +
            "WHERE at.member.id = :memberId " +
            "AND at.endDate IS NOT NULL")
    List<AcademicTitleHistory> findAllByMemberIdAndEndDateNotNull(@Param("memberId") Long memberId);
    @Query("SELECT at FROM AcademicTitleHistory at " +
            "WHERE at.member.id = :memberId " +
            "AND at.endDate IS NULL")
    Optional<AcademicTitleHistory> findCurrentAcademicTitleByMemberId(@Param("memberId") Long memberId);

    List<AcademicTitleHistory> findByMemberIdOrderByStartDate(Long id);
}
