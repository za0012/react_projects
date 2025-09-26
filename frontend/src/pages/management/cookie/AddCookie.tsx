import CookieInput from "@/components/CookieInput";
import { CookieAdd } from "@/types/cookie";
import ky from "ky";
import { useForm } from "react-hook-form";
// import { cookieFields } from "./FormArray";

const AddCookie = () => {
  const { register, control, handleSubmit, formState } = useForm<CookieAdd>({
    defaultValues: {
      name: "",
      // imageUrl: string;
      health: 0,
      ability: "",
      petName: "",
      unlockStarCandies: 0,
      // releaseDate: string; // ISO 형식 날짜
      rarity: "EPIC",
      description: "",
    },
  }); //formState 안의 errors객체로 애러 메세지 표시가 가능함
  const { errors } = formState;

  const cookieFields = [
    {
      name: "name",
      title: "이름",
      type: "text",
      placeholder: "쿠키 이름",
      message: "테테테스트",
      errorMessage: errors.name?.message,
    },
    {
      name: "health",
      title: "체력",
      type: "number",
      placeholder: "쿠키 체력",
      message: "테테테스트",
      errorMessage: errors.health?.message,
    },
    {
      name: "ability",
      title: "능력",
      type: "text",
      placeholder: "쿠키 능력",
      message: "테테테스트dad",
      errorMessage: errors.ability?.message,
    },
    // {
    //   name: "partner",
    //   title: "짝꿍",
    //   type: "text",
    //   placeholder: "짝꿍 쿠키",
    //   message: "테테테스트aa",
    //   errorMessage: errors.partner?.message,
    // },
    {
      name: "petName",
      title: "펫 이름",
      type: "text",
      placeholder: "펫 이름",
      message: "테테테스트vv",
      errorMessage: errors.petName?.message,
    },
    {
      name: "unlockStarCandies",
      title: "별사탕",
      type: "number",
      placeholder: "별사탕 수",
      message: "테테테스d트",
      errorMessage: errors.unlockStarCandies?.message,
    },
    {
      name: "rarity",
      title: "희귀도",
      type: "text",
      placeholder: "희귀도",
      message: "테테테스트",
      errorMessage: errors.rarity?.message,
    },
  ];

  const onSubmit = async (data: CookieAdd) => {
    console.log("Form submitted.", data);
    const response = await ky
      .post("http://localhost:8080/api/cookies", {
        json: {
          name: data.name,
          health: data.health,
          ability: data.ability,
          petName: data.petName,
          unlockStarCandies: data.unlockStarCandies,
          rarity: data.rarity,
          description: data.health,
        },
      })
      .json<CookieAdd>();
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-50 p-8">
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="w-full max-w-xl rounded-2xl bg-white p-10 shadow-xl transition-all duration-300 ease-in-out hover:shadow-2xl"
        noValidate
      >
        <h2 className="mb-8 text-center text-2xl font-extrabold">
          새로운 쿠키 등록
        </h2>
        <div className="space-y-3">
          {cookieFields.map(field => (
            <CookieInput
              key={field.name}
              title={field.title}
              type={field.type}
              name={field.name}
              placeholder={field.placeholder}
              register={register}
              required={field.message}
              errorMessage={field.errorMessage}
            />
          ))}
          <div>
            <label
              htmlFor="description"
              className="block text-sm font-medium text-gray-700"
            >
              설명
            </label>
            <textarea
              id="description"
              {...register("description")}
              placeholder="쿠키에 대한 설명"
              className="w-full rounded-lg border-2 border-gray-200 bg-white px-4 py-3 text-sm transition-colors duration-200 ease-in-out focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-100"
            />
          </div>
          <div className="pt-4">
            <button
              type="submit"
              className="w-full rounded-full bg-gradient-to-r from-blue-500 to-blue-600 py-3 font-semibold text-white shadow-lg transition-all duration-300 ease-in-out hover:scale-[1.01] hover:from-blue-600 hover:to-blue-700 active:scale-95"
            >
              쿠키 추가하기
            </button>
          </div>
        </div>
      </form>
    </div>
  );
};

export default AddCookie;
