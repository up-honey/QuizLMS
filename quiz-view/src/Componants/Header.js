import React from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";

function Header({ isLoggedln, setIsLoggedIn, username, isAdmin, setIsAdmin, checkLoginStatus}) {
    // console.log('Header props:', { isLoggedln, username, isAdmin });
    const location = useLocation(); // Router 내부에서 useLocation을 사용
    const navigate = useNavigate();

    const handleLogout = async (e) => {
        e.preventDefault();

        try{
            const response = await fetch('/logout', {
                method: 'POST', // 스프링 시큐리티 로그아웃은 기본적으로 POST 요청
                //credentials: 'include' // 세션 쿠키를 포함하여 요청
            });

            if(response.ok){
                setIsLoggedIn(false);
                 // 로그인 상태 확인 함수 호출
                await checkLoginStatus();
                setIsAdmin('');
                navigate('/login');
                console.log(`로그아웃 성공, 사용자 이름: ${username}`);
            }else{
                console.error("로그아웃 실패");
            }

        } catch(error) {
            console.error('로그아웃 에러', error);
        }
    };

    return (
        <div className="quiz-header">
            <div className="wrapper">
                <div className="quiz-icon">
                    <Link to="/"><img src="/images/logo.png" alt="QUIZ TEST Logo" className="header-logo" /> </Link>
                </div>
                <nav className="gnb">
                    {isLoggedln && (
                    <div className="nav-links">
                        <Link to="/result/list">내 퀴즈 결과</Link>
                        <Link to="/mypage">마이페이지</Link>
                    </div>
                    )}
                    {/* 관리자인 경우에만 카테고리 메뉴 표시 */}
                    {isAdmin && (
                        <div className="admin-links">
                            <Link to="/category">카테고리 등록</Link>
                            <Link to="/quiz/list">퀴즈 등록</Link>
                        </div>
                    )}
                </nav>
                <div className="quiz-info">
                    {/* 로그인 이름 처리 */}
                    {location.pathname !== '/login' ? (
                        isLoggedln ? (
                            <div>
                                <Link to="/logout" onClick={handleLogout} className="quiz-info-text">로그아웃</Link>
                                <span>{username} 님</span>
                            </div>
                        ) : (
                            <div>
                                <Link to="/login" className="quiz-info-text">로그인</Link>
                                <Link to="/join" className="quiz-info-text">회원가입</Link>
                            </div>
                        )
                        
                    ) : null}
                </div>
            </div>
        </div>
    );
};

export default Header;
