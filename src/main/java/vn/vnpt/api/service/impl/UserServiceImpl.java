package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.out.user.UserListOut;
import vn.vnpt.api.repository.UserRepository;
import vn.vnpt.api.service.UserService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public PagingOut<UserListOut> getAllUsers(SortPageIn sortPageIn, String role) {
        return userRepository.getAllUser(sortPageIn, role);
    }
}
