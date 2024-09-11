import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Header from "./Componants/Header";
import Footer from "./Componants/Footer";
import Login from "./Componants/Login";
import Join from "./Componants/Join";
import QuizSelection from "./Componants/QuizSelection";
import ChatBot from "./Componants/ChatBot";
import ChatBotButton from './Componants/ChatBotButton';
import api from "./Componants/api";
import './App.css';
import './Css/Quiz.css'
import CategoryAll from "./Componants/CategoryAll";
import QuizCreate from './Componants/quiz/QuizCreate';
import QuizModify from './Componants/quiz/QuizModify';
import QuizList from './Componants/quiz/QuizList';
import QuizSubmit from './Componants/quiz/QuizSubmit';

import QuizResults from "./Componants/quiz/QuizResults";

function App() {
  const [isLoggedln, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState('');
  const [isChatBotOpen, setIsChatBotOpen] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);

  const toggleChatBot = () => {
    setIsChatBotOpen(!isChatBotOpen);
  };

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
    checkLoginStatus();
  }, []);

  return (
    <Router>
      <div>
        <Header 
          isLoggedln={isLoggedln} 
          setIsLoggedIn={setIsLoggedIn} 
          username={username} 
          isAdmin={isAdmin} 
          setIsAdmin={setIsAdmin} 
          checkLoginStatus={checkLoginStatus} 
        />
        <div className="App">
          <ChatBotButton onClick={toggleChatBot} />
          <ChatBot isOpen={isChatBotOpen} onClose={() => setIsChatBotOpen(false)} />
        </div>
        <div className="container">
          <Routes>
            <Route path="/" element={<QuizSelection />} />
            <Route path="/login" element={
              <Login 
                setIsLoggedIn={setIsLoggedIn} 
                setUsername={setUsername} 
                setIsAdmin={setIsAdmin} 
                checkLoginStatus={checkLoginStatus} 
              />
            } />
            <Route path="/join" element={<Join />} />
            <Route path="/chat" element={<ChatBot />} />
            <Route path="/categoryAll" element={<CategoryAll />} />
            <Route path="/quiz/list" element={<QuizList />} />
            <Route path="/quiz/create" element={<QuizCreate />} />
            <Route path="/quiz/modify/:id" element={<QuizModify />} />
            <Route path="/quiz/category/:categoryName" element={<QuizSubmit />} />
            <Route path="/quiz/submit" element={<QuizResults />} />
            {isAdmin && (
              <Route path="/category" element={<CategoryAll />} />
            )}
          </Routes>
        </div>
        <Footer />
      </div>
    </Router>
  );
}

export default App;