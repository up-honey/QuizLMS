import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import '../Css/ChatBot.css';
import { FaTimes } from 'react-icons/fa';

const ChatBot = ({ isOpen, onClose }) => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [isRaccoon, setIsRaccoon] = useState(false);
  const chatContainerRef = useRef(null);

  useEffect(() => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    const userMessage = { role: 'user', content: input };
    setMessages(prev => [...prev, userMessage]);
    setInput('');

    if (input === "나는 너가 판다가 아닌것을 안다.") {
      setIsRaccoon(true);
    }

    try {
      console.log('Sending message:', input);
      const response = await axios.post('/api/chat', { message: input });
      console.log('Received response:', response.data);

      const assistantMessage = {
        role: 'assistant',
        content: response.data.response || (isRaccoon ? '응답을 받지 못했습니다너굴.' : '응답을 받지 못했습니다바오.')
      };
      setMessages(prev => [...prev, assistantMessage]);
    } catch (error) {
      console.error('Error sending message:', error);
      console.error('Error response:', error.response);

      const errorMessage = {
        role: 'assistant',
        content: `API 호출 중 오류가 발생했습니다${isRaccoon ? '너굴' : '바오'}: ${error.response ? error.response.status : 'Unknown'} - ${error.response ? error.response.data.error : error.message}`
      };
      setMessages(prev => [...prev, errorMessage]);
    }
  };

  if (!isOpen) return null;

  return (
    <div className={`chat-container ${isOpen ? 'open' : ''}`}>
      <div className="chat-header">
        <img 
          src={isRaccoon
            ? "https://cdn.pixabay.com/photo/2018/11/16/22/27/raccoon-3820327_1280.jpg"
            : "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Grosser_Panda.JPG/640px-Grosser_Panda.JPG"
          }
          alt={isRaccoon ? "너구리" : "판다"}
          className="chat-avatar"
        />
        <h3>{isRaccoon ? '🦝 너굴맨과 공부하기' : '🐼 푸바오와 공부하기'}</h3>
        <button onClick={onClose} className="close-button">
          <FaTimes />
        </button>
      </div>
      <div className="chat-messages" ref={chatContainerRef}>
        {messages.map((msg, index) => (
          <div key={index} className={`message ${msg.role}`}>
            <strong>{msg.role === 'user' ? '👤 당신:' : (isRaccoon ? '🦝 너굴맨:' : '🐼 푸바오:')}</strong> {msg.content}
          </div>
        ))}
      </div>
      <form onSubmit={handleSubmit} className="chat-input-form">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder={`여기에 메시지를 입력해주세요${isRaccoon ? '너굴' : '바오'}...`}
        />
        <button type="submit">전송{isRaccoon ? '너굴' : '바오'}</button>
      </form>
    </div>
  );
};

export default ChatBot;