import React from "react";

const About = () => {
  return (
    <div className="bg-gradient-to-br from-pink-200 via-yellow-100 to-blue-200 p-8 rounded-xl shadow-lg mx-auto mt-10 w-full">
      <h1 className="text-3xl font-extrabold text-pink-600 mb-4 text-center animate-bounce">
        안녕하세요! 멸치액젓입니다!
      </h1>
      <p className="text-lg mb-2 font-semibold text-blue-700">
        해당 사이트는 쿠키런 오븐브레이크와 관련된 정보를 제공합니다!
      </p>
      <p className="mb-2 text-gray-700">
        저도 예전에 새 계정으로 쿠키런을 시작해봤는데요,
      </p>
      <p className="mb-2 text-gray-700">
        뭐부터 해야 할지, 코인 어디서 모으는지, 뉴비한테 좋은 쿠키는 뭔지...
        궁금한 게 정말 많더라고요!
      </p>
      <p className="mb-2 text-gray-700">
        그래서! 궁금증을 해결해드리고 싶어서 이 사이트를 만들었습니다!
      </p>
      <p className="mb-2 text-gray-700">도움이 되셨으면 좋겠습니다😊</p>
      <p className="mt-4 text-base text-purple-700 font-bold">
        혹시 궁금한 점이나 개선할 점이 있다면, 위에 있는 무지개 곰젤리 버튼을
        눌러주세요! <span className="animate-pulse">🌈</span>
      </p>
    </div>
  );
};

export default About;
