package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.AdmissionFile;


@Repository
public interface AdmissionFileRepo extends JpaRepository<AdmissionFile, String> {
    @Modifying
    @Query("DELETE FROM AdmissionFile a WHERE a.schoolYearInformationId.id = :schoolYearInformationId")
    void deleteBySchoolYearInformationId(String schoolYearInformationId);
}
