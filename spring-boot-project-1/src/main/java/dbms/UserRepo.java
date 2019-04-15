package dbms;

import com.crawler.springbootproject1.bean.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends MongoRepository<User, User> {

    public User findById(String id);
    public User findByUsername(String username);
    public List<User> findByName(String name);
    public List<User> findAll();
}
