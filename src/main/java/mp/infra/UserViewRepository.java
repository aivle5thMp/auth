package mp.infra;

import java.util.List;
import mp.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "userViews", path = "userViews")
public interface UserViewRepository
    extends PagingAndSortingRepository<UserView, Long> {
    List<UserView> findByEmail(String email);
}
