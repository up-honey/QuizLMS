import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';

const ChatBot = () => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [isRedPanda, setIsRedPanda] = useState(false);
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
      setIsRedPanda(true);
    }
  
    try {
      console.log('Sending message:', input);
      const response = await axios.post('/api/chat', { message: input });
      console.log('Received response:', response.data);
      
      const assistantMessage = { 
        role: 'assistant', 
        content: response.data.response || '응답을 받지 못했습니다바오.'
      };
      setMessages(prev => [...prev, assistantMessage]);
    } catch (error) {
      console.error('Error sending message:', error);
      console.error('Error response:', error.response);
      
      const errorMessage = { 
        role: 'assistant', 
        content: `API 호출 중 오류가 발생했습니다바오: ${error.response ? error.response.status : 'Unknown'} - ${error.response ? error.response.data.error : error.message}` 
      };
      setMessages(prev => [...prev, errorMessage]);
    }
  };

  return (
    <div className="chat-container">
      <h1>🐼 푸바오와 대화해요</h1>
      <p>안녕하세요! 저는 판다 푸바오예요. 함께 이야기 나눠볼까요바오?</p>
      
      <div className="chat-layout">
        <div className="chat-messages" ref={chatContainerRef}>
          {messages.map((msg, index) => (
            <div key={index} className={`message ${msg.role}`}>
              <strong>{msg.role === 'user' ? '👤 당신:' : '🐼 푸바오:'}</strong> {msg.content}
            </div>
          ))}
        </div>
        <div className="chat-image">
          <img 
            src={isRedPanda 
              ? "https://cdn.pixabay.com/photo/2023/03/01/04/52/ai-generated-7822241_1280.jpg"
              : "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Grosser_Panda.JPG/640px-Grosser_Panda.JPG"
            } 
            alt={isRedPanda ? "래서판다" : "푸바오"}
          />
          <p>{isRedPanda ? "나는 사실 래서판다예요!" : "푸바오예요바오!"}</p>
        </div>
      </div>

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="여기에 메시지를 입력해주세요바오..."
        />
        <button type="submit">전송바오</button>
      </form>
    </div>
  ); 
};

export default ChatBot;

