package in.ashokit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ashokit.entities.CityEntity;

@Repository
public interface CityRepo extends JpaRepository<CityEntity, Integer>{

	public List<CityEntity> findByStateId(Integer stateId);
}
