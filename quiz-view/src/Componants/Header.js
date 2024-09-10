import React, { useState } from "react"; // 0911 useState 가 React에서 상태를 관리하기 위해 사용하는 훅 드랍다운같이 열고 닫는거 하기 위한 임포트
import { Link, useLocation, useNavigate } from "react-router-dom"; // React Router의 Link, useLocation, useNavigate 훅을 임포트
import '../Css/Header.css'; // CSS 파일 임포트

function Header({ isLoggedln, setIsLoggedIn, username, isAdmin, setIsAdmin, checkLoginStatus }) {
    // 로그인 여부와 관리자 여부를 받아와서 헤더에서 보여줄 내용을 결정
    const location = useLocation(); // 현재 경로를 가져오기 위한 훅, Router 내부에서 useLocation을 사용
    const navigate = useNavigate(); // 페이지 이동을 위한 훅
    const [isDropdownOpen, setIsDropdownOpen] = useState(false); // 0911 드랍다운 정의함

    // 로그아웃 함수
    const handleLogout = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('/logout', {
                method: 'POST', // 스프링 시큐리티 로그아웃은 기본적으로 POST 요청
                // credentials: 'include' // 세션 쿠키를 포함하여 요청 (필요한 경우 사용)
            });

            if (response.ok) {
                setIsLoggedIn(false);
                // 로그인 상태 확인 함수 호출
                await checkLoginStatus();
                setIsAdmin(''); // 로그아웃 시 관리자 상태 초기화
                navigate('/login'); // 로그인 페이지로 이동
                console.log(`로그아웃 성공, 사용자 이름: ${username}`);
            } else {
                console.error("로그아웃 실패");
            }

        } catch (error) {
            console.error('로그아웃 에러', error);
        }
    };

    // 드롭다운 열고 닫는 함수 0911
    const toggleDropdown = () => {
        setIsDropdownOpen(!isDropdownOpen);
    };

    return (
        <div className="quiz-header">
            <div className="wrapper">
                <div className="left-menu">
                    <div className="quiz-icon">
                        {/* 메인 페이지로 이동하는 링크 */}
                        <Link to="/">퀴즈맞춰봐</Link>
                    </div>
                    <nav className="gnb">
                        {/* 홈이 아닐 때 생성 */}
                        {/* {location.pathname !== "/" && (
                            <>
                            <Link to="/">Home</Link>
                            </>
                        )} */}
                        {/* 퀴즈 페이지로 가는 링크, 현재 페이지가 퀴즈 페이지가 아닐 때만 표시 */}
                        {location.pathname !== "/quiz" && (
                            <Link to="/quiz">퀴즈 테스트 페이지 이동</Link>
                        )}

                        {/* 챗봇 페이지로 이동하는 링크 */}
                        <Link to="/chat">챗봇</Link>
                        {/* 관리자일 경우에만 관리자 메뉴를 보여줌 */}
                        {isAdmin && (
                            <div className="admin-menu">
                                {/* 관리자 메뉴를 열고 닫는 버튼 */}
                                <button onClick={toggleDropdown} className="admin-menu-button">
                                    관리자 메뉴
                                </button>
                                {/* 드롭다운이 열려 있으면 카테고리 등록 및 퀴즈 목록 링크 표시 */}
                                {isDropdownOpen && (
                                    <div className="admin-menu-dropdown">
                                        <Link to="/category">카테고리 등록</Link>
                                        <Link to="/quiz/list">퀴즈 목록</Link>
                                    </div>
                                )}
                            </div>
                        )}
                    </nav>
                </div>    
                <div className="right-menu">
                    {location.pathname !== '/login' ? (
                        isLoggedln ? (
                            <div className="quiz-info">
                                <Link to="/logout" onClick={handleLogout} className="quiz-info-text">로그아웃</Link>
                                <span>{username} 님</span>
                            </div>
                        ) : (
                            <div className="quiz-info">
                                <Link to="/login" className="quiz-info-text">로그인</Link>
                                <Link to="/join" className="quiz-info-text">회원가입</Link>
                            </div>
                        )
                    ) : null}
                </div>
            </div>
        </div>
    );
}

export default Header;
