package chat.service;

import java.util.List;
import java.util.Optional;

import chat.model.dto.Register;
import chat.model.dto.UserDto;
import chat.model.dto.Password;
import chat.model.dto.Profile;

public interface UserService {
	// 註冊會員
	Register addUser(Register register);

	// 查詢所有會員
	List<Register> findAllUser();

	// 查詢個人資料(Profile)
	Profile getUser(String username);
	
	//查詢個人資料(UserDto)
	UserDto findByUser(String username);
	
	//查詢所有資料(針對群組增加會員)
	List<UserDto> finAllUserDtos();

	// 修改個人資料
	Profile updateProfile(Profile profile);
	
	//修改密碼
	Boolean updatePassword(String username,Password password);

	// 登入
	Optional<Register> checkPassword(Register register);
}
