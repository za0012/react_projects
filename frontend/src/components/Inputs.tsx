interface inputType {
  title: string;
  type: string;
  name: string;
  placeholder: string;
  register: any;
  required: string;
  errorMessage: any;
}

const Inputs = ({
  title,
  type,
  name,
  placeholder,
  register,
  required,
  errorMessage,
}: inputType) => {
  return (
    <div className="flex flex-col gap-1">
      <label htmlFor={name} className="block text-sm font-medium text-gray-700">
        {title}
      </label>
      <input
        type={type}
        id={name}
        {...register(name, { required })}
        placeholder={placeholder}
        className="w-full rounded-lg border-2 border-gray-200 bg-white px-4 py-3 text-sm transition-colors duration-200 ease-in-out focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-100"
      />
      <p className="min-h-[20px] text-xs font-semibold text-red-500">
        {errorMessage}
      </p>
    </div>
  );
};

export default Inputs;
