import type { ReservationDto, UserDto } from "../../types/api";

const CalendarIcon = () => <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="w-5 h-5 text-slate-400"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>;
const ClockIcon = () => <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="w-5 h-5 text-slate-400"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>;
const UsersIcon = () => <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="w-5 h-5 text-slate-400"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>;
const HourglassIcon = () => <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="w-5 h-5 text-slate-400"><path d="M5 22h14" /><path d="M5 2h14" /><path d="M17 22v-4.172a2 2 0 0 0-.586-1.414L12 12l-4.414 4.414A2 2 0 0 0 7 17.828V22" /><path d="M7 2v4.172a2 2 0 0 0 .586 1.414L12 12l4.414-4.414A2 2 0 0 0 17 6.172V2" /></svg>;
const TableIcon = () => <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="w-5 h-5 text-slate-400"><path d="M2 10h20" /><path d="M2 16h20" /><path d="M12 2v20" /><path d="M6 2v20" /><path d="M18 2v20" /></svg>;
const CheckCircleIcon = () => <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="w-5 h-5 text-slate-400"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" /><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>;
const CheckIcon = () => <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>;
const XIcon = () => <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>;

// Status Badge Component
const StatusBadge = ({ status }: { status: "PENDING" | "ACCEPTED" | "COMPLETED" | "CANCELLED" }) => {
    const statusStyles = {
        PENDING: "bg-amber-100 text-amber-800",
        ACCEPTED: "bg-sky-100 text-sky-800",
        COMPLETED: "bg-slate-200 text-slate-800",
        CANCELLED: "bg-red-100 text-red-800",
    };

    return (
        <span className={`px-3 py-1 text-xs font-semibold rounded-full ${statusStyles[status]}`}>
            {status}
        </span>
    );
};

interface ReservationCardProps {
    reservation: ReservationDto;
    user: UserDto | null;
    onAccept: (reservation: ReservationDto) => void;
    onDecline: (reservation: ReservationDto) => void;
}

const ReservationCard = ({ reservation, user, onAccept, onDecline }: ReservationCardProps) => {
    const {
        email,
        datetime,
        number_of_people,
        stay_length,
        status,
        assigned_table_number,
        front_staff_name: front_staff_name
    } = reservation;

    const reservationDate = new Date(datetime);
    const dateOptions = { year: 'numeric', month: 'long', day: 'numeric' };

    return (
        <div className="bg-white rounded-xl shadow-md overflow-hidden transform hover:-translate-y-1 hover:shadow-lg transition-all duration-300 flex flex-col">
            {/* Card Header */}
            <div className="p-4 md:p-5 border-b border-slate-200 flex justify-between items-start">
                <h3 className="font-bold text-lg text-slate-800 truncate pr-2" title={email}>{email}</h3>
                <StatusBadge status={status} />
            </div>

            {/* Card Body */}
            <div className="p-4 md:p-5 flex-grow">
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-y-4 gap-x-2">
                    <div className="flex items-center space-x-3">
                        <CalendarIcon />
                        <span className="text-sm text-slate-600">{reservationDate.toLocaleDateString(undefined, dateOptions)}</span>
                    </div>
                    <div className="flex items-center space-x-3">
                        <ClockIcon />
                        <span className="text-sm text-slate-600">{reservationDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                    </div>
                    <div className="flex items-center space-x-3">
                        <UsersIcon />
                        <span className="text-sm text-slate-600">{number_of_people} Guests</span>
                    </div>
                    {stay_length ? <div className="flex items-center space-x-3">
                        <HourglassIcon />
                        <span className="text-sm text-slate-600">{stay_length} min{stay_length > 1 ? 's' : ''} stay</span>
                    </div> : <span className="text-sm text-slate-400 italic">No stay length specified</span>}
                </div>

                {status === 'ACCEPTED' && (
                    <div className="mt-4 pt-4 border-t border-slate-200/60 space-y-4">
                        {assigned_table_number && (
                            <div className="flex items-center space-x-3">
                                <TableIcon />
                                <span className="text-sm text-slate-600 font-medium">Assigned Table: <span className="font-bold text-slate-800">#{assigned_table_number}</span></span>
                            </div>
                        )}
                        {front_staff_name && (
                            <div className="flex items-center space-x-3">
                                <CheckCircleIcon />
                                <span className="text-sm text-slate-600">Accepted by: <span className="font-medium text-slate-800 truncate">{front_staff_name}</span></span>
                            </div>
                        )}
                    </div>
                )}
            </div>

            {/* Card Footer with Actions */}
            {user?.user_type === "FRONT_STAFF" && status === "PENDING" && (
                <div className="px-4 md:px-5 py-3 bg-slate-50 flex space-x-2 justify-end">
                    <button
                        onClick={() => onDecline(reservation)}
                        className="px-3 py-2 text-sm font-medium text-red-700 bg-red-100 rounded-lg hover:bg-red-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition-colors duration-200 flex items-center space-x-1.5"
                    >
                        <XIcon />
                        <span>Decline</span>
                    </button>
                    <button
                        onClick={() => onAccept(reservation)}
                        className="px-3 py-2 text-sm font-medium text-green-700 bg-green-100 rounded-lg hover:bg-green-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition-colors duration-200 flex items-center space-x-1.5"
                    >
                        <CheckIcon />
                        <span>Accept</span>
                    </button>
                </div>
            )}
        </div>
    );
};

export default ReservationCard;
