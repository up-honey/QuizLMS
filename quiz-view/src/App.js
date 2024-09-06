import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Quiz from "./Componants/Quiz";
import Header from "./Componants/Header";
import Footer from "./Componants/Footer";
import Login from "./Componants/Login";

function App() {
  const [isLoggedln, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState('');

  return (
    <Router>
      <div>
        {/* Header는 모든 페이지에서 공통으로 보여줍니다 */}
        <Header isLoggedln={isLoggedln} setIsLoggedIn={setIsLoggedIn} username={username} />
        {/* <Login setIsLoggedIn={setIsLoggedIn} setUsername={setUsername} /> */}
        <div className="container">
          <Routes>
            {/* 홈 경로 */}
            <Route path="/" element={<div>Welcome to the Quiz App!</div>} />
            {/* 퀴즈 경로 */}
            <Route path="/quiz" element={<Quiz />} />
            {/* 로그인 경로 */}
            <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} setUsername={setUsername} />} />
          </Routes>
        </div>
        {/* Footer는 모든 페이지에서 공통으로 보여줍니다 */}
        <Footer />
      </div>
    </Router>
  );
}

export default App;
