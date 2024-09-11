import React from 'react';
import '../Css/ChatBotButton.css';

const ChatBotButton = ({ onClick }) => {
    return (
        <button className="chat-bot-button" onClick={onClick}>
            💬 채팅 시작
        </button>
    );
};

export default ChatBotButton;