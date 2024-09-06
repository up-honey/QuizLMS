import React from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";

function Header({ isLoggedln, setIsLoggedIn, username}) {
    const location = useLocation(); // Router 내부에서 useLocation을 사용
    const navigate = useNavigate();

    const handleLogout = async (e) => {
        e.preventDefault();

        try{
            const response = await fetch('/logout', {
                method: 'POST', // 스프링 시큐리티 로그아웃은 기본적으로 POST 요청
                credentials: 'include' // 세션 쿠키를 포함하여 요청
            });

            if(response.ok){
                setIsLoggedIn(false);
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
                    퀴즈 맞추기
                </div>
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
                <nav className="gnb">
                    {/* 홈이 아닐 때 생성 */}
                    {location.pathname !== "/" && (
                        <>
                        <Link to="/">Home</Link>
                        </>
                    )}

                    {/* 현재 경로가 /quiz가 아닌 경우에만 '퀴즈 테스트 페이지 이동' 링크를 표시 */}
                    {location.pathname !== "/quiz" && (
                        <>
                        <Link to="/quiz">퀴즈 테스트 페이지 이동</Link>
                        </>
                    )}
                </nav>
            </div>
        </div>
    );
};

export default Header;
