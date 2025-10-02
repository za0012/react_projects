import Inputs from "@/components/Inputs";
import { PetDetail } from "@/types/pet";
import { useRouter } from "@tanstack/react-router";
import ky from "ky";
import { useForm } from "react-hook-form";

const AddPets = () => {
  const { register, formState, handleSubmit } = useForm<PetDetail>({
    defaultValues: {
      name: "",
      // imageUrl: ;
      ability: "",
      // releaseDate: ;
      rarity: "EPIC",
      description: "",
    },
  });
  const { errors } = formState;
  const router = useRouter();

  const onSubmit = async (data: PetDetail) => {
    console.log(data);
    try {
      const response = await ky.post("http://localhost:8080/api/pets", {
        json: {
          name: data.name,
          // imageUrl: ;
          ability: data.ability,
          // releaseDate: ;
          rarity: data.rarity,
          description: data.description,
        },
      });
    } catch (error) {
      console.error(error);
    }
  };

  const petInputs = [
    {
      name: "name",
      title: "이름",
      type: "text",
      placeholder: "펫 이름",
      message: "테테테스트2",
      errorMessage: errors.name?.message,
    },
    {
      name: "ability",
      title: "능력",
      type: "text",
      placeholder: "펫 능력",
      message: "테테테스트1",
      errorMessage: errors.name?.message,
    },
    {
      name: "rarity",
      title: "희귀도",
      type: "text",
      placeholder: "희귀도",
      message: "COMMON | RARE | EPIC | LEGENDARY | SPECIAL 만 가능합니다",
      errorMessage: errors.rarity?.message,
    },
  ];

  return (
    <div className="relative flex min-h-screen justify-center bg-gray-50 p-8">
      <button
        onClick={() => router.history.back()}
        className="absolute left-8 top-8 z-10 flex h-10 w-10 items-center justify-center rounded-full bg-white text-gray-700 shadow-md transition-all duration-300 hover:bg-gray-100 hover:shadow-lg active:scale-95"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-5 w-5"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={2}
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M15 19l-7-7 7-7"
          />
        </svg>
      </button>
      <div className="flex w-full items-center justify-center">
        <form
          onSubmit={handleSubmit(onSubmit)}
          className="w-full max-w-xl rounded-2xl bg-white p-10 shadow-xl transition-all duration-300 ease-in-out hover:shadow-2xl"
        >
          <h2 className="mb-8 text-center text-2xl font-extrabold">
            새로운 펫 등록
          </h2>
          <div className="space-y-3">
            {petInputs.map(field => (
              <Inputs
                key={field.name}
                title={field.title}
                type={field.type}
                name={field.name}
                placeholder={field.placeholder}
                register={register}
                required={""}
                errorMessage={undefined}
              />
            ))}
            <textarea
              id="description"
              {...register("description")}
              placeholder="펫에 대한 설명"
              rows={4}
              className="w-full rounded-lg border-2 border-gray-200 bg-white px-4 py-3 text-sm transition-colors duration-200 ease-in-out focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-100"
            />
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
    </div>
  );
};

export default AddPets;
