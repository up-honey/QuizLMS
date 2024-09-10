import React from "react";
import "../Css/Footer.css";
import { FaInstagram, FaFacebook, FaYoutube, FaEnvelope, FaPhone, FaMapMarkerAlt } from "react-icons/fa";
import { FaXTwitter } from "react-icons/fa6";

function Footer() {
    return (
        <footer className="footer">
            <div className="footer-content">
                <div className="footer-section about">
                    <img src="/images/logo.png" alt="QUIZ TEST Logo" className="footer-logo" /> 
                    <p>QUIZ TEST는 혁신적인 온라인 학습 플랫폼으로, 사용자들에게 다양한 퀴즈와 테스트를 제공합니다.</p>
                </div>
                <div className="footer-section links">
                    <h2>빠른 링크</h2>
                    <ul>
                        <li><a href="/about">회사 소개</a></li>
                        <li><a href="/services">서비스</a></li>
                        <li><a href="/pricing">요금제</a></li>
                        <li><a href="/faq">자주 묻는 질문</a></li>
                        <li><a href="/privacy">개인정보 처리방침</a></li>
                        <li><a href="/terms">이용약관</a></li>
                    </ul>
                </div>
                <div className="footer-section contact">
                    <h2>연락처</h2>
                    <p><FaPhone /> 010-1234-1234</p>
                    <p><FaEnvelope /> hello@hello.com</p>
                    <p><FaMapMarkerAlt /> 대구광역시 한국 it </p>

                    <div className="socials">
                        <a href="https://x.com" target="_blank" rel="noopener noreferrer"><FaXTwitter /></a>
                        <a href="https://instagram.com" target="_blank" rel="noopener noreferrer"><FaInstagram /></a>
                        <a href="https://facebook.com" target="_blank" rel="noopener noreferrer"><FaFacebook /></a>
                        <a href="https://youtube.com" target="_blank" rel="noopener noreferrer"><FaYoutube /></a>
                    </div>
                </div>
            </div>
            <div className="footer-bottom">
                <p className="copyright">&copy; {new Date().getFullYear()} QUIZ TEST. All rights reserved.</p>
                <div className="footer-bottom-links">
                    <a href="/sitemap">사이트맵</a>
                    <select className="language-select">
                        <option value="ko">한국어</option>
                        <option value="en">English</option>
                        <option value="ja">日本語</option>
                    </select>
                </div>
            </div>
        </footer>
    );
};

export default Footer;