import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Quiz from "./Componants/Quiz";
import Header from "./Componants/Header";
import Footer from "./Componants/Footer";
import Login from "./Componants/Login";
import Join from "./Componants/Join";
import QuizSelection from "./Componants/QuizSelection";
import ChatBot from "./Componants/ChatBot"; // 새로 추가한 ChatBot 컴포넌트
import api from "./Componants/api";
import CategoryAll from "./Componants/CategoryAll"; // 카테고리 컴포넌트 import
import QuizCreate from './Componants/quiz/QuizCreate'; // 퀴즈 등록 컴포넌트 import
import QuizModify from './Componants/quiz/QuizModify'; // 퀴즈 수정 컴포넌트 import
import QuizList from './Componants/quiz/QuizList'; // 퀴즈 목록 컴포넌트 import
import QuizSubmit from './Componants/quiz/QuizSubmit'; // 퀴즈 제출 컴포넌트 import
import QuizResult from "./Componants/QuizResult";

function App() {
  const [isLoggedln, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState('');
  const [isAdmin, setIsAdmin] = useState(false); // 관리자 여부

  // 로그인 상태 확인 함수
  const checkLoginStatus = async () => {
    try {
      const response = await api.get('/api/members/check');
      if (response.status === 200 && response.data.loggedIn && response.data.username !== "anonymousUser") {
        setIsLoggedIn(true);
        setUsername(response.data.username);

        if (response.data.roles && Array.isArray(response.data.roles) && response.data.roles.includes("ROLE_ADMIN")) {
          console.log("트루 누구?", response.data.roles);
          setIsAdmin(true);
        } else {
          console.log("폴스 누구?", response.data.roles);
          setIsAdmin(false);
        }

      } else {
        setIsLoggedIn(false);
        setIsAdmin(false);
      }
    } catch (error) {
      console.error('로그인 상태 확인 오류:', error);
      setIsLoggedIn(false);
      setIsAdmin(false);
    }
  };

  useEffect(() => {
    checkLoginStatus(); // 페이지 로드 시 로그인 상태 확인
  }, []);

  return (
    <Router>
      <div>
        {/* Header는 모든 페이지에서 공통으로 보여줍니다 */}
        <Header isLoggedln={isLoggedln} setIsLoggedIn={setIsLoggedIn} username={username} isAdmin={isAdmin} setIsAdmin={setIsAdmin} checkLoginStatus={checkLoginStatus} />
        <div className="container">
          <Routes>
            {/* 홈 경로 */}
            <Route path="/" element={<QuizSelection />} />
            {/* 퀴즈 경로 */}
            <Route path="/quiz" element={<Quiz />} />
            {/* 로그인 경로 */}
            <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} setUsername={setUsername} setIsAdmin={setIsAdmin} checkLoginStatus={checkLoginStatus} />} />
            {/* 회원가입 경로 */}
            <Route path="/join" element={<Join />} />
            {/* 새로운 ChatBot 라우트 */}
            <Route path="/chat" element={<ChatBot />} />
            {/* 카테고리 경로 */}
            <Route path="/categoryAll" element={<CategoryAll />} />
            {/* 퀴즈 목록 경로 */}
            <Route path="/quiz/list" element={<QuizList />} />
            {/* 퀴즈 등록 경로 */}
            <Route path="/quiz/create" element={<QuizCreate />} />
            {/* 퀴즈 수정 경로 */}
            <Route path="/quiz/modify/:id" element={<QuizModify />} />
            {/* 퀴즈 풀기 경로 */}
            <Route path="/quiz/category/:categoryName" element={<QuizSubmit />} />

             {/* 퀴즈 결과 경로 */}
            <Route path="/quiz/result/list" element={<QuizResult />} />

            {/* 관리자인 경우에만 카테고리 경로가 렌더됨 */}
            {isAdmin && (
              <Route path="/category" element={<CategoryAll />} />
            )}
          </Routes>
        </div>
        {/* Footer는 모든 페이지에서 공통으로 보여줍니다 */}
        <Footer />
      </div>
    </Router>
  );
}

export default App;
