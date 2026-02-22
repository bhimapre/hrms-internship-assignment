import React, { useState } from "react";
import { Link } from "react-router-dom";
import { ChevronDown, ChevronRight, Menu, X, BriefcaseBusiness, Gamepad, Car, TreePine } from "lucide-react";

const Sidebar = ({ isCollapsed, setIsCollapsed }: { isCollapsed: any, setIsCollapsed: any }) => {

  const [openItem, setOpenItem] = useState(null);
  const role = localStorage.getItem("role");

  const items = [
    {
      title: "Games",
      icon: <Gamepad size={20} />,
      children: [
        { name: "Game", path: "/games" },
        { name: "My Booking", path: "/upcoming-booking" },
        { name: "All Games", path: "/hr/game", roles: "HR" },
        { name: "Add Games", path: "/hr/game/add", roles: "HR" },
        { name: "All Game Config", path: "/hr/game-config", roles: "HR" },
        { name: "Add Game Config", path: "/hr/game-config/add", roles: "HR" },
      ],
    },
    {
      title: "Job Opening",
      icon: <BriefcaseBusiness size={20} />,
      children: [
        { name: "JobOpenings", path: "/job-openings" },
        { name: "Job Referral", path: "/hr/job-referral", roles: "HR" },
        { name: "All Job Referral", path: "/hr/job-referral", roles: "HR" },
        { name: "HR Job Opening", path: "/hr/job-opening", roles: "HR" },
        { name: "Add Job Opening", path: "/job-opening/add", roles: "HR" },
      ],
    },
    {
      title: "Travel",
      icon: <Car size={20} />,
      children: [
        { name: "Travel", path: "/travel-list" },
        { name: "Add Travel", path: "/hr/travel-opening/add", roles: "HR" },
      ],
    },
    {
      title: "Organization Chart",
      icon: <TreePine size={20} />,
      children: [
        { name: "Organization chart", path: "/org-chart" },
      ],
    },
  ];

  const toggleItem = (index: any) => {
    setOpenItem(openItem === index ? null : index);
  };

  return (
    <div
      className={`h-[calc(100vh-56px)] bg-neutral-900 text-neutral-200 border-r border-neutral-700 transform transition-all duration-300
      ${isCollapsed ? "w-16" : "w-64"} overflow-y-auto`}>
      {/* Main Layout */}
      <div className="p-4 border-b border-neutral-700 flex justify-between items-center">
        {!isCollapsed && (
          <h2 className="text-xl font-bold tracking-wide">HRMS</h2>
        )}
        <button
          onClick={() => setIsCollapsed(!isCollapsed)}
          className="p-2 rounded-lg hover:bg-neutral-800 transition ml-auto">
          {isCollapsed ? <Menu size={18} /> : <X size={18} />}
        </button>
      </div>

      {/* Menu Items */}
      <ul className="p-2 space-y-2">
        {items.map((item, index) => (
          <li key={index}>
            <button
              onClick={() => toggleItem(index)}
              className="w-full flex items-center p-2 rounded-lg hover:bg-neutral-800 transition">
              <span className="mr-2">{item.icon}</span>
              {!isCollapsed && (
                <span className="flex-1 text-left">{item.title}</span>
              )}
              {!isCollapsed &&
                (openItem === index ? (
                  <ChevronDown size={16} />
                ) : (
                  <ChevronRight size={16} />
                ))}
            </button>

            {!isCollapsed && openItem === index && (
              <ul className="ml-8 mt-1 space-y-1 text-sm text-neutral-400">
                {item.children.filter(child => !child.roles || child.roles.includes(role!)).map((child, i) => (
                  <li key={i}>
                    <Link
                      to={child.path}
                      className="block p-2 rounded hover:text-neutral-100 hover:bg-neutral-800 transition">
                      {child.name}
                    </Link>
                  </li>
                ))}
              </ul>
            )}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default Sidebar