package in.ashokit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ashokit.entities.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer>{
 
  public UserEntity  findByEmail(String email);
  
  public UserEntity findByEmailAndPwd(String email,String pwd);
  
  
}
