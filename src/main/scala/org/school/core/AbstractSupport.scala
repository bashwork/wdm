package org.school.core

trait AbstractSupport {
    /**
     * Retrieves the requested value for the specified key
     *
     * @param key The key to find the value for
     * @return the value for the specified input
     */
    def get(key:String) : Double
}
